package wbif.sjx.common.HighContent.Module;

import wbif.sjx.common.HighContent.Object.*;


/**
 * Returns a spherical object around a point object.  This is useful for calculating local object features.
 */
public class GetLocalObjectRegion implements Module {
    public static final String INPUT_OBJECTS = "Input objects";
    public static final String OUTPUT_OBJECTS = "Output objects";
    public static final String LOCAL_RADIUS = "Local radius";
    public static final String CALIBRATED_RADIUS = "Calibrated radius";

    @Override
    public void execute(Workspace workspace, ParameterCollection parameters, boolean verbose) {
        if (verbose) System.out.println("   Calculating local volume around object centroids");

        // Getting input objects
        HCObjectName inputObjectsName = parameters.getValue(this,INPUT_OBJECTS);
        HCObjectSet inputObjects = workspace.getObjects().get(inputObjectsName);

        // Getting output objects name
        HCObjectName outputObjectsName = parameters.getValue(this,OUTPUT_OBJECTS);

        // Getting parameters
        boolean calibrated = parameters.getValue(this,CALIBRATED_RADIUS);
        double radius = parameters.getValue(this,LOCAL_RADIUS);
        if (verbose & calibrated) System.out.println("       Using local radius of "+radius+" px");
        if (verbose & !calibrated) System.out.println("       Using local radius of "+radius+" ");

        // Getting local region
        HCObjectSet outputObjects = getLocalRegions(inputObjects, radius, calibrated);

        // Adding output objects to workspace
        workspace.addObjects(outputObjectsName,outputObjects);
        if (verbose) System.out.println("       Adding objects ("+outputObjectsName+") to workspace");

    }

    @Override
    public ParameterCollection initialiseParameters() {
        ParameterCollection parameters = new ParameterCollection();

        parameters.addParameter(new Parameter(this,INPUT_OBJECTS,Parameter.OBJECT_NAME,"Obj1",false));
        parameters.addParameter(new Parameter(this,OUTPUT_OBJECTS,Parameter.OBJECT_NAME,"Obj2",false));
        parameters.addParameter(new Parameter(this,LOCAL_RADIUS,Parameter.DOUBLE,10.0,true));
        parameters.addParameter(new Parameter(this,CALIBRATED_RADIUS,Parameter.BOOLEAN,false,false));

        return parameters;

    }

    public static HCObjectSet getLocalRegions(HCObjectSet inputObjects, double radius, boolean calibrated) {
        // Creating store for output objects
        HCObjectSet outputObjects = new HCObjectSet();

        // Running through each object, calculating the local texture
        for (HCObject inputObject:inputObjects.values()) {
            // Creating new object and assigning relationship to input objects
            HCObject outputObject = new HCObject(inputObject.getID());
            outputObject.setParent(inputObject);
            inputObject.addChild(outputObject);

            // Getting image calibration (to deal with different xy-z dimensions)
            double xCal = inputObject.getCalibration(HCObject.X);
            double yCal = inputObject.getCalibration(HCObject.Y);
            double zCal = inputObject.getCalibration(HCObject.Z);

            double xy_z_ratio = xCal/zCal;

            // Getting centroid coordinates
            double xCent = MeasureObjectCentroid.calculateCentroid(inputObject.getCoordinates(HCObject.X),MeasureObjectCentroid.MEAN);
            double yCent = MeasureObjectCentroid.calculateCentroid(inputObject.getCoordinates(HCObject.Y),MeasureObjectCentroid.MEAN);
            double zCent = inputObject.getCoordinates(HCObject.Z) != null
                    ? MeasureObjectCentroid.calculateCentroid(inputObject.getCoordinates(HCObject.Z), MeasureObjectCentroid.MEAN)
                    : 0;

            if (calibrated) {
                for (int x = (int) Math.floor(xCent - radius/xCal); x <= (int) Math.ceil(xCent + radius/xCal); x++) {
                    for (int y = (int) Math.floor(yCent - radius/yCal); y <= (int) Math.ceil(yCent + radius/yCal); y++) {
                        for (int z = (int) Math.floor(zCent - radius/zCal); z <= (int) Math.ceil(zCent + radius/zCal); z++) {
                            if (Math.sqrt((xCent-x)*xCal*(xCent-x)*xCal + (yCent-y)*yCal*(yCent-y)*yCal + (zCent-z)*zCal*(zCent-z)*zCal) < radius) {
                                outputObject.addCoordinate(HCObject.X, x);
                                outputObject.addCoordinate(HCObject.Y, y);
                                outputObject.addCoordinate(HCObject.Z, z);

                            }
                        }
                    }
                }

            } else {
                for (int x = (int) Math.floor(xCent - radius); x <= (int) Math.ceil(xCent + radius); x++) {
                    for (int y = (int) Math.floor(yCent - radius); y <= (int) Math.ceil(yCent + radius); y++) {
                        for (int z = (int) Math.floor(zCent - radius * xy_z_ratio); z <= (int) Math.ceil(zCent + radius * xy_z_ratio); z++) {
                            if (Math.sqrt((xCent-x)*(xCent-x) + (yCent-y)*(yCent-y) + (zCent-z)*(zCent-z)/(xy_z_ratio*xy_z_ratio)) < radius) {
                                outputObject.addCoordinate(HCObject.X, x);
                                outputObject.addCoordinate(HCObject.Y, y);
                                outputObject.addCoordinate(HCObject.Z, z);

                            }
                        }
                    }
                }
            }

            // Adding object to HashMap
            outputObjects.put(outputObject.getID(),outputObject);

        }

        return outputObjects;

    }

}

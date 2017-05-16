package wbif.sjx.common.HighContent.Module;

import wbif.sjx.common.HighContent.Object.*;
import wbif.sjx.common.HighContent.Object.HCParameterCollection;

/**
 * Created by sc13967 on 04/05/2017.
 */
public class ProjectImage extends HCModule {
    public static final String INPUT_IMAGE = "Input image";
    public static final String OUTPUT_IMAGE = "Output image";

    @Override
    public void execute(HCWorkspace workspace, boolean verbose) {
        // Loading image into workspace
        HCImageName inputImageName = parameters.getValue(INPUT_IMAGE);
        HCImage inputImage = workspace.getImages().get(inputImageName);

        // Getting output image name
        HCImageName outputImageName = parameters.getValue(OUTPUT_IMAGE);

        // Create max projection image
        HCImage outputImage = inputImage.projectImageInZ();

        // Adding projected image to workspace
        workspace.addImage(outputImageName,outputImage);

    }

    @Override
    public HCParameterCollection initialiseParameters() {
        HCParameterCollection parameters = new HCParameterCollection();

        parameters.addParameter(new HCParameter(this,MODULE_TITLE, HCParameter.MODULE_TITLE,"Image projector",false));
        parameters.addParameter(new HCParameter(this,INPUT_IMAGE, HCParameter.INPUT_IMAGE,null,false));
        parameters.addParameter(new HCParameter(this,OUTPUT_IMAGE, HCParameter.OUTPUT_IMAGE,null,false));

        return parameters;

    }

    @Override
    public HCParameterCollection getActiveParameters() {
        return parameters;
    }
}

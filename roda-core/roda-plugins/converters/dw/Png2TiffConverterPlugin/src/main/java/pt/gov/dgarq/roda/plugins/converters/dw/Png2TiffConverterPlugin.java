package pt.gov.dgarq.roda.plugins.converters.dw;

import org.apache.log4j.Logger;

import pt.gov.dgarq.roda.core.data.RepresentationObject;
import pt.gov.dgarq.roda.core.plugins.Plugin;
import pt.gov.dgarq.roda.plugins.converters.common.AbstractRodaMigratorPlugin;

/**
 * @author Rui Castro
 */
public class Png2TiffConverterPlugin extends AbstractRodaMigratorPlugin {
	private static Logger logger = Logger
			.getLogger(Png2TiffConverterPlugin.class);

	private final String name = "Converter/PNG to TIFF";
	private final float version = 1.0f;
	private final String description = "Digitalized Work PNG representations normalizer plugin. "
			+ "Convert DigitalizedWork representations with PNG images to uncompressed TIFFs using RODA Migrator service DW2Tiff.";

	/**
	 * 
	 * @see Plugin#getName()
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @see Plugin#getVersion()
	 */
	public float getVersion() {
		return version;
	}

	/**
	 * @see Plugin#getDescription()
	 */
	public String getDescription() {
		return description;
	}

	@Override
	protected String getConfigurationFile() {
		return "png2tiff-converter.properties";
	}

	/**
	 * 
	 * @see AbstractRodaMigratorPlugin#getRepresentationType ()
	 */
	@Override
	protected String getRepresentationType() {
		return RepresentationObject.DIGITALIZED_WORK;
	}

	/**
	 * 
	 * @see AbstractRodaMigratorPlugin#isNormalization()
	 */
	@Override
	protected boolean isNormalization() {
		return true;
	}

}

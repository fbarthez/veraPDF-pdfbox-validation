package org.verapdf.model;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.model.impl.pb.cos.PBCosDocument;
import org.verapdf.pdfa.ValidationModelParser;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

/**
 * Current class is entry point to model implementation.
 *
 * @author Evgeniy Muravitskiy
 */
public final class ModelParser implements ValidationModelParser, Closeable {

    private static final Logger LOGGER = Logger.getLogger(ModelParser.class);

    private PDDocument document;

    private final PDFAFlavour flavour;

    /**
     * @param toLoad
     * @throws IOException
     */
    public ModelParser(InputStream toLoad, PDFAFlavour flavour) throws IOException {
        this.document = PDDocument.load(toLoad, false, true);
        this.flavour = flavour;
    }

    /**
     * Get {@code PDDocument} object for current file.
     *
     * @return {@link org.apache.pdfbox.pdmodel.PDDocument} object of pdfbox
     *         library.
     * @throws IOException
     *             when target file is not pdf or pdf file is not contain root
     *             object
     */
    public PDDocument getPDDocument() throws IOException {
        return this.document;
    }

    /**
     * Method return root object of model implementation from pdf box model
     * together with the hierarchy.
     *
     * @return root object representing by
     *         {@link org.verapdf.model.coslayer.CosDocument}
     * @throws IOException
     *             when target file is not pdf or pdf file is not contain root
     *             object
     */
    @Override
    public org.verapdf.model.baselayer.Object getRoot() throws IOException {
        return new PBCosDocument(this.document, this.flavour);
    }

	@Override
	public void close() {
		try {
            if (this.document != null) {
                this.document.close();
            }
		} catch (IOException e) {
            LOGGER.error("Problems with document close.", e);
        }
	}
}
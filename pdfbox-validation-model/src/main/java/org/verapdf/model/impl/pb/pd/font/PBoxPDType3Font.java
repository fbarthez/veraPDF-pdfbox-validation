package org.verapdf.model.impl.pb.pd.font;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFontLike;
import org.apache.pdfbox.pdmodel.font.PDType3CharProc;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.pb.pd.PBoxPDContentStream;
import org.verapdf.model.pdlayer.PDContentStream;
import org.verapdf.model.pdlayer.PDType3Font;
import org.verapdf.model.tools.resources.PDInheritableResources;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author Timur Kamalov
 */
public class PBoxPDType3Font extends PBoxPDSimpleFont implements PDType3Font {

    public static final String TYPE3_FONT_TYPE = "PDType3Font";

    public static final String CHAR_STRINGS = "charStrings";

	private final PDInheritableResources resources;

	private final PDDocument document;
	private final PDFAFlavour flavour;

	private List<PDContentStream> charStrings = null;
	private boolean containsTransparency = false;

	public PBoxPDType3Font(PDFontLike font, PDInheritableResources resources, PDDocument document, PDFAFlavour flavour) {
		super(font, TYPE3_FONT_TYPE);
		this.resources = resources;
		this.document = document;
		this.flavour = flavour;
	}

	@Override
	public Boolean getisStandard() {
		return Boolean.FALSE;
	}

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        if (CHAR_STRINGS.equals(link)) {
            return this.getCharStrings();
        }
        return super.getLinkedObjects(link);
    }

    private List<PDContentStream> getCharStrings() {
        if (this.charStrings == null) {
			parseCharStrings();
		}
		return this.charStrings;
    }

	/**
	 * @return true if any of charproc content streams contains transparency
	 */
	public boolean isContainsTransparency() {
		if (this.charStrings == null) {
			parseCharStrings();
		}
		return this.containsTransparency;
	}

	private void parseCharStrings() {
		COSDictionary charProcDict = ((org.apache.pdfbox.pdmodel.font.PDType3Font) this.pdFontLike)
				.getCharProcs();
		if (charProcDict != null) {
			Set<COSName> keySet = charProcDict.keySet();
			List<PDContentStream> list = new ArrayList<>(keySet.size());
			for (COSName cosName : keySet) {
				PDType3CharProc charProc = ((org.apache.pdfbox.pdmodel.font.PDType3Font) this.pdFontLike)
						.getCharProc(cosName);
				PBoxPDContentStream contentStream =
						new PBoxPDContentStream(charProc, this.resources, this.document, this.flavour);
				this.containsTransparency |= contentStream.isContainsTransparency();
				list.add(contentStream);
			}
			this.charStrings = Collections.unmodifiableList(list);
		} else {
			this.charStrings = Collections.emptyList();
		}
	}
}

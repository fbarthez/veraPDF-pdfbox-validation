package org.verapdf.model.impl.pb.operator.textshow;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosReal;
import org.verapdf.model.impl.pb.cos.PBCosReal;
import org.verapdf.model.operator.Op_DoubleQuote;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_DoubleQuote extends PBOpStringTextShow implements Op_DoubleQuote{

	public static final String OP_DOUBLIE_QUOTE_TYPE = "Op_DoubleQuote";

	public static final String WORD_SPACING = "wordSpacing";
	public static final String CHARACTER_SPACING = "characterSpacing";
	public static final int COUNT_OF_OPERATOR_OPERANDS = 3;

	public PBOp_DoubleQuote(List<COSBase> arguments, PDFont font) {
		super(arguments, font);
		setType(OP_DOUBLIE_QUOTE_TYPE);
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		List<? extends Object> list;

		switch (link) {
			case WORD_SPACING:
				list = getWordSpacing();
				break;
			case CHARACTER_SPACING:
				list = getCharacterSpacing();
				break;
			default:
				list = super.getLinkedObjects(link);
				break;
		}

		return list;
	}

	private List<CosReal> getWordSpacing() {
		return getSpecialReal(0);
	}

	private List<CosReal> getCharacterSpacing() {
		return getSpecialReal(1);
	}

	private List<CosReal> getSpecialReal(int operandNumber) {
		List<CosReal> characterSpacing = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
		if (this.arguments.size() >= COUNT_OF_OPERATOR_OPERANDS) {
			int index = (this.arguments.size() - COUNT_OF_OPERATOR_OPERANDS + operandNumber);
			COSBase base = this.arguments.get(index);
			if (base instanceof COSNumber) {
				characterSpacing.add(new PBCosReal((COSNumber) base));
			}
		}
		return characterSpacing;
	}
}

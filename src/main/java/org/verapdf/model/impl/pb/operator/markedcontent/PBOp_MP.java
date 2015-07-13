package org.verapdf.model.impl.pb.operator.markedcontent;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.Op_MP;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_MP extends PBOpMarkedContent implements Op_MP {

    public static final String OP_MP_TYPE = "Op_MP";

    public PBOp_MP(List<COSBase> arguments) {
        super(arguments);
        setType(OP_MP_TYPE);
    }

	@Override
	public List<? extends org.verapdf.model.baselayer.Object> getLinkedObjects(String link) {
		List<? extends org.verapdf.model.baselayer.Object> list;

		switch (link) {
			case TAG:
				list = this.getTag();
				break;
			default: list = super.getLinkedObjects(link);
		}

		return list;
	}

}

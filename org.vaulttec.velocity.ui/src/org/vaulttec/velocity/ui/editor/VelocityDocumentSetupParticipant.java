package org.vaulttec.velocity.ui.editor;

import org.eclipse.core.filebuffers.IDocumentSetupParticipant;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.vaulttec.velocity.ui.editor.text.IVelocityPartitions;
import org.vaulttec.velocity.ui.editor.text.VelocityPartitionScanner;

public class VelocityDocumentSetupParticipant implements IDocumentSetupParticipant {

	@Override
	public void setup(IDocument document) {
		IDocumentPartitioner partitioner = new FastPartitioner(new VelocityPartitionScanner(),
				IVelocityPartitions.PARTITIONS);
		if (document instanceof IDocumentExtension3) {
			IDocumentExtension3 extension3 = (IDocumentExtension3) document;
			extension3.setDocumentPartitioner(IVelocityPartitions.VELOCITY_PARTITIONING, partitioner);
		} else {
			document.setDocumentPartitioner(partitioner);
		}
		partitioner.connect(document);
	}

}

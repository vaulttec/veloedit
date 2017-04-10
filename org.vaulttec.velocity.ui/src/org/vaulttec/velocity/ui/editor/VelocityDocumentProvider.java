/*******************************************************************************
 * Copyright (c) 2003 Torsten Juergeleit.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 * 
 * The Eclipse Public License is available at
 *    http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 *    http://www.eclipse.org/org/documents/edl-v10.html.
 * 
 * Contributors:
 *     Torsten Juergeleit - initial API and implementation
 *******************************************************************************/
package org.vaulttec.velocity.ui.editor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.editors.text.ForwardingDocumentProvider;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.vaulttec.velocity.ui.editor.text.IVelocityPartitions;
import org.vaulttec.velocity.ui.editor.text.VelocityPartitionScanner;

/**
 * This class provides the {@link IDocument}s used by Velocity editors. These
 * {@link IDocument}s have an Velocity-aware partition scanner (multi-line
 * comments) attached.
 * 
 * @see VelocityPartitionScanner
 */
public class VelocityDocumentProvider extends TextFileDocumentProvider {

	public VelocityDocumentProvider() {
		IDocumentProvider provider = new TextFileDocumentProvider();
		provider = new ForwardingDocumentProvider(IVelocityPartitions.VELOCITY_PARTITIONING,
				new VelocityDocumentSetupParticipant(), provider);
		setParentDocumentProvider(provider);
	}
}

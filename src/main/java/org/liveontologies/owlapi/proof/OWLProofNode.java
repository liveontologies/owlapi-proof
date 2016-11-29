package org.liveontologies.owlapi.proof;

/*-
 * #%L
 * OWL API Proof Extension
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2014 - 2016 Live Ontologies Project
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.Collection;

import org.liveontologies.proof.util.ProofNode;
import org.semanticweb.owlapi.model.OWLAxiom;

/**
 * Represents (possibly recursive) derivations for the given {@link OWLAxiom}
 * member. All {@link OWLProofStep} inferences returned by
 * {@link #getInferences()} must have this member as a conclusion, but it is not
 * required that there should be at least one such an inference.
 * 
 * @author Yevgeny Kazakov
 */
public interface OWLProofNode extends ProofNode<OWLAxiom> {

	@Override
	OWLAxiom getMember();

	@Override
	Collection<? extends OWLProofStep> getInferences();

}

package org.liveontologies.owlapi.proof.util;

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

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

/**
 * TODO
 * 
 * @author Pavel Klinov
 *
 * pavel.klinov@uni-ulm.de
 */
public class ProofTestUtils {
	
	private static final OWLDataFactory FACTORY = OWLManager.getOWLDataFactory();
	private static final String PREFIX = "http://example.com/";

	public static ProofNode<OWLAxiom> generateRandomProofGraph(Random rnd, int maxInferencesForExpression, int maxPremisesForInference, int totalExpressionLimit) {
		int exprCount = 0;		
		Queue<OWLAxiom> toDo = new ArrayDeque<OWLAxiom>();
		OWLAxiom root = FACTORY.getOWLSubClassOfAxiom(clazz("sub"), clazz("sup"));
		MockProof<OWLAxiom> proof = MockProof.create(); 
		
		toDo.add(root);
		
		for (;;) {
			OWLAxiom next = toDo.poll();
			
			if (next == null) {
				break;
			}
			// 0 inferences is allowed
			int numOfInf = rnd.nextInt(maxInferencesForExpression + 1);
			
			for (int i = 0; i < numOfInf; i++) {
				// generate new "inference"
				int numOfPremises = 1 + rnd.nextInt(maxPremisesForInference);
				MockProof<OWLAxiom>.MockProofStepBuilder builder = proof
						.conclusion(next);
				
				for (int j = 0; j < numOfPremises; j++) {
					OWLClass sub = FACTORY.getOWLClass(IRI.create("http://random.org/" + randomString(rnd, 4)));
					OWLClass sup = FACTORY.getOWLClass(IRI.create("http://random.org/" + randomString(rnd, 4)));
					OWLSubClassOfAxiom premise = FACTORY
							.getOWLSubClassOfAxiom(sub, sup);
					builder.premise(premise);
					toDo.add(premise);
					exprCount++;
				}
				
				builder.build();
			}
			
			if (exprCount >= totalExpressionLimit) {
				break;
			}
		}
		
		return proof.getNode(root);
	}
	
	private static String randomString(Random rnd, int len) {
		StringBuilder builder = new StringBuilder(len);
		String alphabet = "abc1de2fg3hi4jk5lmn6opq7rs8tuv9wx0yz";
		
		for (int i = 0; i < len; i++) {
			builder.append(alphabet.charAt(rnd.nextInt(alphabet.length())));
		}
			
		return builder.toString();
	}

	private static OWLClass clazz(String suffix) {
		return FACTORY.getOWLClass(IRI.create(PREFIX + suffix));
	}
	
//	public static <C> ProofNode<C> pickRandomExpression(final ProofNode<C> root, final Random rnd) {
//		/*// traverses the tree twice, probably slow but should be OK for testing
//		int size = getProofGraphSize(root);
//		
//		if (size <= 1) {
//			// can't pick the root so exit
//			return null;
//		}
//		
//		// pick the i_th according to some fixed order, doesn't pick the root
//		final int index = 1 + rnd.nextInt(size - 1);
//		final AtomicInteger counter = new AtomicInteger(0);
//		final AtomicReference<OWLExpression> ref = new AtomicReference<OWLExpression>();
//		
//		OWLProofUtils.visitExpressionsInProofGraph(root, new OWLExpressionVisitor<Void>() {
//
//			@Override
//			public Void visit(OWLAxiomExpression expression) {
//				if (counter.get() == index) {
//					ref.set((OWLExpression) expression); 
//				}
//				
//				counter.incrementAndGet();
//				
//				return null;
//			}
//
//			@Override
//			public Void visit(OWLLemmaExpression expression) {
//				counter.incrementAndGet();
//				return null;
//			}
//			
//		});
//		
//		return ref.get();*/
//		
//		if (!root.getInferences().iterator().hasNext()) {
//			// never pick the root
//			return null;
//		}
//		
//		final AtomicInteger counter = new AtomicInteger(0);
//		final AtomicReference<ProofNode<C>> ref = new AtomicReference<ProofNode<C>>();
//		
//		OWLProofUtils.visitExpressionsInProofGraph(root, new ProofNode<C>.Visitor<Void>() {
//
//			@Override
//			public Void visit(ProofNode<C> expression) {
//				int cnt = counter.incrementAndGet();
//				
//				if (ref.get() == null) {
//					ref.set(expression);
//				}
//				else {
//					double chance = 1d / cnt;
//					
//					if (chance > rnd.nextDouble()) {
//						ref.set(expression);
//					}
//				}
//				
//				return null;
//			}
//			
//		});
//		
//		return ref.get();
//	}
//	
//	public static int getProofGraphSize(ProofNode<C> root) {
//		final AtomicInteger counter = new AtomicInteger();
//		
//		OWLProofUtils.visitExpressionsInProofGraph(root, new ProofNode<C>.Visitor<Void>() {
//
//			@Override
//			public Void visit(ProofNode<C> expression) {
//				counter.incrementAndGet();
//				return null;
//			}
//			
//		});
//		
//		return counter.get();
//	}
}

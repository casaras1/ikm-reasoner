/*
 * #%L
 * ELK Reasoner
 * 
 * $Id$
 * $HeadURL$
 * %%
 * Copyright (C) 2011 Department of Computer Science, University of Oxford
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
package org.semanticweb.elk.reasoner.saturation.rules.factories;

import org.semanticweb.elk.reasoner.indexing.hierarchy.IndexedClassExpression;
import org.semanticweb.elk.reasoner.saturation.ContextCreationListener;
import org.semanticweb.elk.reasoner.saturation.ContextModificationListener;
import org.semanticweb.elk.reasoner.saturation.SaturationState;
import org.semanticweb.elk.reasoner.saturation.SaturationStateWriter;
import org.semanticweb.elk.reasoner.saturation.SaturationStatistics;
import org.semanticweb.elk.reasoner.saturation.SaturationUtils;
import org.semanticweb.elk.reasoner.saturation.conclusions.visitors.ComposedConclusionVisitor;
import org.semanticweb.elk.reasoner.saturation.conclusions.visitors.ConclusionInsertionVisitor;
import org.semanticweb.elk.reasoner.saturation.conclusions.visitors.ConclusionSourceContextNotSaturatedCheckingVisitor;
import org.semanticweb.elk.reasoner.saturation.conclusions.visitors.ConclusionVisitor;
import org.semanticweb.elk.reasoner.saturation.conclusions.visitors.NonRedundantRuleApplicationConclusionVisitor;
import org.semanticweb.elk.reasoner.saturation.context.Context;
import org.semanticweb.elk.reasoner.saturation.rules.RuleVisitor;
import org.semanticweb.elk.util.concurrent.computation.InputProcessor;

/**
 * The factory for engines for concurrently computing the saturation of class
 * expressions. This is the class that implements the application of inference
 * rules.
 * 
 * @author Frantisek Simancik
 * @author Yevgeny Kazakov
 * @author Markus Kroetzsch
 * @author Pavel Klinov
 * 
 */
public class RuleApplicationFactory extends AbstractRuleApplicationFactory {

	public RuleApplicationFactory(SaturationState saturationState) {
		super(saturationState);
	}

	public InputProcessor<IndexedClassExpression> getEngine(
			ContextCreationListener listener,
			ContextModificationListener modListener) {
		SaturationStatistics localStatistics = new SaturationStatistics();
		listener = SaturationUtils.addStatsToContextCreationListener(listener,
				localStatistics.getContextStatistics());
		modListener = SaturationUtils.addStatsToContextModificationListener(
				modListener, localStatistics.getContextStatistics());
		SaturationStateWriter writer = saturationState.getExtendedWriter(
				listener, modListener);
		writer = SaturationUtils.getStatsAwareWriter(writer, localStatistics);
		return super.getEngine(writer, localStatistics);
	}

	@Override
	@SuppressWarnings("unchecked")
	ConclusionVisitor<Context, Boolean> getConclusionProcessor(
			RuleVisitor ruleVisitor, SaturationStateWriter writer) {
		return new ComposedConclusionVisitor<Context>(
		// insert conclusions
				new ConclusionInsertionVisitor(),
				// if new, check that conclusion is not for a saturated context
				new ConclusionSourceContextNotSaturatedCheckingVisitor(
						saturationState),
				// apply the non-redundant rules
				new NonRedundantRuleApplicationConclusionVisitor(ruleVisitor,
						writer));
	}
}

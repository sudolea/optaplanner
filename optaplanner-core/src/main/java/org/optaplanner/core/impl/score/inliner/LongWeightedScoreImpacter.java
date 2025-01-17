/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
 *
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
 */

package org.optaplanner.core.impl.score.inliner;

import java.util.function.Consumer;

import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.impl.score.director.InnerScoreDirector;

@FunctionalInterface
public interface LongWeightedScoreImpacter extends WeightedScoreImpacter {

    /**
     * @param matchWeight never null
     * @param scoreConsumer null if {@link InnerScoreDirector#isConstraintMatchEnabled()} is false
     * @return never null
     */
    UndoScoreImpacter impactScore(long matchWeight, Consumer<Score<?>> scoreConsumer);

}

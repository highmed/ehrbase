/*
 * Modifications copyright (C) 2019 Christian Chevalley, Vitasystems GmbH and Hannover Medical School

 * This file is part of Project EHRbase

 * Copyright (c) 2015 Christian Chevalley
 * This file is part of Project Ethercis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ehrbase.aql.compiler;

import org.ehrbase.dao.jooq.impl.DSLContextHelper;
import org.jooq.DSLContext;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by christian on 4/1/2016.
 */

public class AqlExpressionTest {


    private DSLContext context = DSLContextHelper.buildContext();



    @Test
    public void testDump() {

        String query = "SELECT o/data[at0002]/events[at0003] AS systolic\n" +
                "FROM EHR [ehr_id/value='1234'] \n" +
                "CONTAINS COMPOSITION c [openEHR-EHR-COMPOSITION.encounter.v1] \n" +
                "CONTAINS OBSERVATION o [openEHR-EHR-OBSERVATION.blood_pressure.v1]\n" +
                "WHERE o/data[at0001]/events[at0006]/data[at0003]/items[at0004]/value/value > 140";


        assertThat(new AqlExpression().parse(query).dump()).isEqualTo("(query (queryExpr (select SELECT (selectExpr (identifiedPath o / (objectPath (pathPart data (predicate [ (nodePredicateOr (nodePredicateAnd (nodePredicateComparable at0002))) ])) / (pathPart events (predicate [ (nodePredicateOr (nodePredicateAnd (nodePredicateComparable at0003))) ])))) AS systolic)) (from FROM (fromEHR EHR (standardPredicate [ (predicateExpr (predicateAnd (predicateEquality (predicateOperand (objectPath (pathPart ehr_id) / (pathPart value))) = (predicateOperand (operand '1234'))))) ])) CONTAINS (containsExpression (containExpressionBool (contains (simpleClassExpr (archetypedClassExpr COMPOSITION c [ openEHR-EHR-COMPOSITION.encounter.v1 ])) CONTAINS (containsExpression (containExpressionBool (contains (simpleClassExpr (archetypedClassExpr OBSERVATION o [ openEHR-EHR-OBSERVATION.blood_pressure.v1 ]))))))))) (where WHERE (identifiedExpr (identifiedEquality (identifiedOperand (identifiedPath o / (objectPath (pathPart data (predicate [ (nodePredicateOr (nodePredicateAnd (nodePredicateComparable at0001))) ])) / (pathPart events (predicate [ (nodePredicateOr (nodePredicateAnd (nodePredicateComparable at0006))) ])) / (pathPart data (predicate [ (nodePredicateOr (nodePredicateAnd (nodePredicateComparable at0003))) ])) / (pathPart items (predicate [ (nodePredicateOr (nodePredicateAnd (nodePredicateComparable at0004))) ])) / (pathPart value) / (pathPart value)))) > (identifiedOperand (operand 140))))) <EOF>))");
    }

    @Test
    public void testPass1() {

        String query = "SELECT o/data[at0002]/events[at0003] AS systolic\n" +
                "FROM EHR [ehr_id/value='1234'] \n" +
                "CONTAINS COMPOSITION c [openEHR-EHR-COMPOSITION.encounter.v1] \n" +
                "CONTAINS OBSERVATION o [openEHR-EHR-OBSERVATION.blood_pressure.v1]\n" +
                "WHERE o/data[at0001]/events[at0006]/data[at0003]/items[at0004]/value/value > 140";

        AqlExpression cut = new AqlExpression().parse(query);
        Contains contains = new Contains(cut.getParseTree()).process();

        assertThat(contains.getIdentifierMapper()).isNotNull();
        assertThat(contains.getNestedSets()).isNotNull();
        assertThat(contains.getContainClause()).isNotNull();
    }

    @Test
    public void testPass2() {

        String query = "SELECT o/data[at0002]/events[at0003] AS systolic\n" +
                "FROM EHR [ehr_id/value='1234'] \n" +
                "CONTAINS COMPOSITION c [openEHR-EHR-COMPOSITION.encounter.v1] \n" +
                "CONTAINS OBSERVATION o [openEHR-EHR-OBSERVATION.blood_pressure.v1]\n" +
                "WHERE o/data[at0001]/events[at0006]/data[at0003]/items[at0004]/value/value > 140";

        AqlExpression cut = new AqlExpression().parse(query);

        Statements statements = new Statements(cut.getParseTree(), new Contains(cut.getParseTree()).process().getIdentifierMapper()).process() ;

        assertThat(statements.getVariables()).isNotNull();
        assertThat(statements.getWhereClause()).isNotNull();

    }


}
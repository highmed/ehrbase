# Copyright (c) 2019 Wladislaw Wagner (Vitasystems GmbH), Pablo Pazos (Hannover Medical School).
#
# This file is part of Project EHRbase
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.



*** Settings ***
Documentation   Composition Integration Tests

Resource    ${CURDIR}${/}../../_resources/keywords/composition_keywords.robot

# Resource    ${CURDIR}${/}../_resources/suite_settings.robot
# Resource    ${CURDIR}${/}../_resources/keywords/generic_keywords.robot
# Resource    ${CURDIR}${/}../_resources/keywords/template_opt1.4_keywords.robot
# Resource    ${CURDIR}${/}../_resources/keywords/ehr_keywords.robot



Force Tags    JSON



*** Test Cases ***
Main flow get existing VERSIONED COMPOSITION

    upload OPT    minimal/minimal_observation.opt

    create EHR

    commit composition (JSON)    minimal/minimal_observation.composition.participations.extdatetimes.xml

    # Check COMPOSITION exists (by versioned_object_uid)
    composition_keywords.start request session    # Prefer=return\=representation
    get versioned composition by uid    ${versioned_object_uid}
    check content of versioned composition (JSON)

    # [Teardown]    restart SUT

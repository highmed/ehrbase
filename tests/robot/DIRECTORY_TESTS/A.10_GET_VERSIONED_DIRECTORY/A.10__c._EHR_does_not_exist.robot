*** Settings ***
Documentation    Alternative flow 2: get versioned directory from non existent EHR
...
...     Preconditions:
...         None
...
...     Flow:
...         1. Invoke the has directory service for a random ehr_id
...         2. The service should return an error related with the non existence of the EHR
...
...     Postconditions:
...         None


Resource    ${CURDIR}${/}../../_resources/suite_settings.robot
Resource    ${CURDIR}${/}../../_resources/keywords/generic_keywords.robot
Resource    ${CURDIR}${/}../../_resources/keywords/contribution_keywords.robot
Resource    ${CURDIR}${/}../../_resources/keywords/directory_keywords.robot
Resource    ${CURDIR}${/}../../_resources/keywords/template_opt1.4_keywords.robot
Resource    ${CURDIR}${/}../../_resources/keywords/ehr_keywords.robot

#Suite Setup  startup SUT
# Test Setup  start openehr server
# Test Teardown  restore clean SUT state
#Suite Teardown  shutdown SUT

Force Tags    refactor



*** Test Cases ***
Alternative flow 2: get versioned directory from non-existent EHR

    create fake EHR

    get DIRECTORY - fake ehr_id (JSON)

    check response: is negative - EHR does not exist
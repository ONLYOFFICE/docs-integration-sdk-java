package com.onlyoffice.service.callback;

public final class CallbackServiceDataTest {

    private CallbackServiceDataTest() { };

    public static final String CALLBACK_STATUS_EDITING = "{\n" +
            "    \"actions\": [{\"type\": 1, \"userid\": \"78e1e841\"}],\n" +
            "    \"key\": \"Khirz6zTPdfd7\",\n" +
            "    \"status\": 1,\n" +
            "    \"users\": [\"6d5a81d0\", \"78e1e841\"]\n" +
            "}";
    public static final String CALLBACK_STATUS_EDITING_WITH_TOKEN = "{\"key\":\"89ee3209-8142-422d-95bb-5ee8c76a3065_1.0\",\"status\":1,\"users\":[\"admin\"],\"actions\":[{\"type\":1,\"userid\":\"admin\"}],\"token\":\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJrZXkiOiI4OWVlMzIwOS04MTQyLTQyMmQtOTViYi01ZWU4Yzc2YTMwNjVfMS4wIiwic3RhdHVzIjoxLCJ1c2VycyI6WyJhZG1pbiJdLCJhY3Rpb25zIjpbeyJ0eXBlIjoxLCJ1c2VyaWQiOiJhZG1pbiJ9XX0._3FAyl9pB-j8cRUHXwjXFO-C2kTCgBSIUeq2kpXzrAY\"}";

    public static final String AUTHORIZATION_HEADER_STATUS_EDITING = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJwYXlsb2FkIjp7ImtleSI6Ijg5ZWUzMjA5LTgxNDItNDIyZC05NWJiLTVlZThjNzZhMzA2NV8xLjAiLCJzdGF0dXMiOjQsImFjdGlvbnMiOlt7InR5cGUiOjAsInVzZXJpZCI6ImFkbWluIn1dfX0.aIuduI-8ewe4UeMGMXVlSatSpMelbnO2YYZSiWo2Ctk";

    public static final String CALLBACK_STATUS_SAVE = "{\n" +
            "    \"actions\": [{\"type\": 0, \"userid\": \"78e1e841\"}],\n" +
            "    \"changesurl\": \"https://documentserver/url-to-changes.zip\",\n" +
            "    \"history\": {\n" +
            "        \"changes\": [],\n" +
            "        \"serverVersion\": \"7.0.0\"\n" +
            "    },\n" +
            "    \"filetype\": \"docx\",\n" +
            "    \"key\": \"Khirz6zTPdfd7\",\n" +
            "    \"status\": 2,\n" +
            "    \"url\": \"https://documentserver/url-to-edited-document.docx\",\n" +
            "    \"users\": [\"6d5a81d0\"]\n" +
            "}";

    public static final String CALLBACK_STATUS_CLOSED = "{\n" +
            "    \"key\": \"Khirz6zTPdfd7\",\n" +
            "    \"status\": 4\n" +
            "}";

    public static final String CALLBACK_STATUS_FORCESAVE = "{\n" +
            "    \"changesurl\": \"https://documentserver/url-to-changes.zip\",\n" +
            "    \"forcesavetype\": 0,\n" +
            "    \"history\": {\n" +
            "        \"changes\": [],\n" +
            "        \"serverVersion\": \"7.0.0\"\n" +
            "    },\n" +
            "    \"filetype\": \"docx\",\n" +
            "    \"key\": \"Khirz6zTPdfd7\",\n" +
            "    \"status\": 6,\n" +
            "    \"url\": \"https://documentserver/url-to-edited-document.docx\",\n" +
            "    \"users\": [\"6d5a81d0\"],\n" +
            "    \"userdata\": \"sample userdata\"\n" +
            "}";
}

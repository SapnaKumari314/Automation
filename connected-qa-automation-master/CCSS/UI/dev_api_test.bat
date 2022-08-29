cd CCSS_UI_API_Automation

mvn clean install test -Dtest.Type=API -Denv.Name=dev -Dgarage.Name=Centennial -DsuiteXmlFile=apiTestng.xml

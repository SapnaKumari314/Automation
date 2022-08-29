cd CCSS_UI_API_Automation

mvn clean install test -Dtest.Type=API -Denv.Name=ets-test -Dgarage.Name=Centennial -DsuiteXmlFile=apiTestng.xml

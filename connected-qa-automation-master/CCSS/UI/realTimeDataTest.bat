cd CCSS_UI_API_Automation

mvn clean install test -Dtest.Type=API -Denv.Name=qa -Dgarage.Name=Centennial -DsuiteXmlFile=realTimeData.xml

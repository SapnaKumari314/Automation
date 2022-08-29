cd CCSS_UI_API_Automation

mvn clean install test -Dtest.Type=UI -Denv.Name=qa -Dbrowser.headless=true -Dtestng.dtd.http=true -Dbrowser.type=chrome -DsuiteXmlFile=testng_smoke.xml

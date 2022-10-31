pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
            echo 'Building..'
            git branch: 'main', credentialsId: 'a8b0b31a-f7bc-4450-ac60-63b530ff0311', url: 'https://github.com/Sapnak3/test-repo.git'   
                               }
        }
            
        stage('Test') {
            steps {
                catchError {
                
                echo 'Testing..'
                
                bat '"C:\\Program Files\\Eggplant\\runscript.bat" "C:\\ProgramData\\Jenkins\\.jenkins\\workspace\\FunctionalTesting\\Eggplant_Automation\\CCSS_Edmonton.suite\\Scripts\\TestCases\\Demo\\TOUC_3301.script" -CommandLineOutput Yes -AlertOnError yes -LicenserHost ec2-54-210-217-115.compute-1.amazonaws.com'
                    
                }  
                
            }
        }
        
       
        }
    }

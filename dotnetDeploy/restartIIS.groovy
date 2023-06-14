pipeline {
    agent {
        label 'agent'
    }

    environment {
        REMOTE_MACHINE_NAME = '172.31.4.252'
        CRED_ID = 'server_cred'
        // machines = ['172.31.4.252', '172.31.2.206']
        // creds = [
        //     "3.22.41.76": 'server_cred',
        //     "172.31.2.206": 'value2',
        // ]
    }

    stages {
        stage('Create PowerShell session') {
            steps {
                withCredentials([usernamePassword(credentialsId: env.CRED_ID, usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                    powershell '''
                        $session = New-PSSession -ComputerName '3.22.41.76' -Credential (New-Object System.Management.Automation.PSCredential("$env:USERNAME", $(ConvertTo-SecureString "$env:PASSWORD" -AsPlainText -Force)))
                        Invoke-Command -Session $session -ScriptBlock {stop-WebAppPool -Name "DefaultAppPool"}
                        '''
                }
            }
        }
        
    
    }
}
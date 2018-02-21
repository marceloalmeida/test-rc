node {
    stage('Checkout') {
        timeout(time: 10, unit:'MINUTES'){
          checkout scm
        }
      }
      
      stage('tag') {
        withCredentials([usernamePassword(credentialsId: 'jumia-integrations-token', passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
            env.tag_name = env.BRANCH_NAME + '-' + env.BUILD_ID
            sh('git rev-parse HEAD |tr -d "\n" > commit_id')
            env.commit_id = readFile 'commit_id'
            print env.commit_id
            sh('''
                curl --max-time 10 --silent --user "${GIT_USERNAME}:${GIT_PASSWORD}" -H "Content-Type: application/json" -X POST -d '{"tag_name": "'${tag_name}'", "target_commitish": "'${commit_id}'"}' "https://api.github.com/repos/marcelosousaalmeida/test-rc/releases"
            ''')
        }
    }
}


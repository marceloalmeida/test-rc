node {
    stage('Checkout') {
        timeout(time: 10, unit:'MINUTES'){
            checkout scm
        }
    }

    lib_release = load "./lib_test.groovy"

    stage('tag') {
        //withCredentials([usernamePassword(credentialsId: 'jumia-integrations-token', passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
        //    env.tag_name = env.BRANCH_NAME + '-' + env.BUILD_ID
        //    def commitID = sh(returnStdout: true, script: 'git log -1 --pretty=format:"%H"').trim()
        //    env.commit_id = readFile 'commit_id'
        //    print env.commit_id
        //    sh('''
        //        curl --max-time 10 --silent --user "${GIT_USERNAME}:${GIT_PASSWORD}" -H "Content-Type: application/json" -X POST -d '{"tag_name": "'${tag_name}'", "target_commitish": "'${commit_id}'"}' "https://api.github.com/repos/marcelosousaalmeida/test-rc/releases"
        //    ''')
        //}
        String tagName = env.BRANCH_NAME + '-' + env.BUILD_ID
        String commitID = sh(returnStdout: true, script: 'git log -1 --pretty=format:"%H"').trim()
        status = lib_release.createRelease(tagName, commitID,"test-rc", "marcelosousaalmeida")
    }
}


node {
    try {
        stage('Checkout') {
            timeout(time: 10, unit:'MINUTES'){
                checkout scm
            }
        }

        lib_release = load "./lib_test.groovy"

        stage('tag') {
            String tagName = env.BRANCH_NAME + '-' + env.BUILD_ID
            String commitID = sh(returnStdout: true, script: 'git log -1 --pretty=format:"%H"').trim()

            status = lib_release.createRelease(tagName, commitID,"test-rc", "marcelosousaalmeida")

            if status {
                currentBuild.description = "<a href=''>Release</a>"
            }
        }
    } catch (err) {
        print err.getMessage()
        currentBuild.result = 'FAILURE'
    } finally {
        step([
            $class: 'WsCleanup',
            deleteDirs: true,
            patterns: [
                [
                    pattern: '.git/**',
                    type: 'EXCLUDE'
                ]
            ]
        ])
    }
}

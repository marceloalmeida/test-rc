def createRelease(tagName, commitID, name="", body="", draft=false, prerelease=false) {
    String owner = "marcelosousaalmeida"
    String repo = "test-rc"

    if (name == "") {
        name = tagName
    }
    withCredentials([usernamePassword(credentialsId: 'jumia-integrations-token', usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_PASSWORD')]) {
        String callAPI = """
        set -ex;
        curl -s -m 10 -XPOST \
            --user ${GIT_USERNAME}:{GIT_PASSWORD}
            --url https://api.github.com/repos/${owner}/${repo}/releases
            --header "Content-Type: application/json"
            --data \'{
                "tag_name": "${tagName}",
                "target_commitish": "${commitID},
                "name": "${name}",
                "body": "${body}",
                "draft": ${draft},
                "prerelease": ${prerelease}
            "}\' > /tmp/${tagName}_${commitID}.txt
        """

        print(callAPI)

        status = sh(returnStatus: true, script: callAPI)
    }

    print(status)

    return status
}

this

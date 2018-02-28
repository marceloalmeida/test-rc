def createRelease(tagName, commitID, repo, owner="JumiaAIG", body="", name=tagName, draft=false, prerelease=false, gitCredentials='jumia-integrations-token') {
    String releaseURL = ""

    if (name == "") {
        name = tagName
    }

    withCredentials([usernamePassword(credentialsId: gitCredentials, usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_PASSWORD')]) {
        String callAPI = """
            set +x && \
            curl -f -s -m 10 -X POST \
                --user ${GIT_USERNAME}:${GIT_PASSWORD} \
                --url https://api.github.com/repos/${owner}/${repo}/releases \
                --header "Content-Type: application/json" \
                --data '{
                    "tag_name": "${tagName}",
                    "target_commitish": "${commitID}",
                    "name": "${name}",
                    "body": '${body}',
                    "draft": ${draft},
                    "prerelease": ${prerelease}
                }' | \
                jq -r ".html_url" | tee release_URL.txt
        """

        print(callAPI)

        status = sh(returnStatus: true, script: callAPI)
    }

    if (status == 0) {
        releaseURL = readFile(encoding: 'UTF-8', file: './release_URL.txt')
    }

    return releaseURL
}

def getChangelog() {
    String changelog = "Changelog:\n"
    def changeLogSets = currentBuild.changeSets
    int changeID = 1

    for (int i = 0; i < changeLogSets.size(); i++) {
        def entries = changeLogSets[i].items
        for (int j = 0; j < entries.length; j++) {
            def entry = entries[j]
            changelog += "${changeID}. ${entry.msg}\\n"
            changeID++
        }
    }

    print(changelog.trim())

    return changelog.trim()
}

return this

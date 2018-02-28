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
                    "body": "${body}",
                    "draft": ${draft},
                    "prerelease": ${prerelease}
                }' | \
                jq -r ".html_url" | tee release_URL.txt
        """

        status = sh(returnStatus: true, script: callAPI)
    }

    if (status == 0) {
        releaseURL = readFile(encoding: 'UTF-8', file: './release_URL.txt')
    }

    return releaseURL
}

def getChangelog() {
    String changelog = "Changelog:\\n"
    Boolean emptyChanges = true
    def build = currentBuild
    int changeID = 1

    while(build != null && build.result != 'SUCCESS') {
        for (changeLog in build.changeSets) {
            for(entry in changeLog.items) {
                emptyChanges = true
                changelog += "${changeID}. ${entry.msg}\\n"
                changeID++
            }
        }
        build = build.previousBuild
    }

    if (emptyChanges) {
        changelog += "Empty changes."
    }

    return changelog.trim()
}

return this

import hudson.FilePath
import org.eclipse.jgit.transport.URIish

node {
    env.WORKSPACE = pwd()
    stage 'Checkout'
        checkout scm

        def build = manager.build
        def listener = manager.listener
        def workspace = new FilePath(new File(env.WORKSPACE))
        def environment = build.getEnvironment(listener)
        final def project = build.getParent()
        final def gitScm = project.getTypicalSCM()
        final def gitClient = gitScm.createClient(listener, environment, build, workspace);

        final def gitTagName = "TAG_NAME"
        final def comment = "COMMENT"
        final def remoteURI = new URIish("origin")

        gitClient.tag(gitTagName, comment)
        gitClient.push().tags(true).to(remoteURI).execute()
}

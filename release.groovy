#!/usr/bin/groovy
def stage(){
  return stageProject{
    project = 'fabric8-apps/keycloak-app'
    useGitTagForNextVersion = true
  }
}

def approveRelease(project){
  def releaseVersion = project[1]
  approve{
    room = null
    version = releaseVersion
    console = null
    environment = 'fabric8'
  }
}

def release(project){
  releaseProject{
    stagedProject = project
    useGitTagForNextVersion = true
    helmPush = false
    groupId = 'io.fabric8.apps'
    githubOrganisation = 'fabric8-apps'
    artifactIdToWatchInCentral = 'keycloak-app'
    artifactExtensionToWatchInCentral = 'pom'
    promoteToDockerRegistry = 'docker.io'
    dockerOrganisation = 'fabric8'
    imagesToPromoteToDockerHub = []
    extraImagesToTag = null
  }
}

def updateDownstreamDependencies(stagedProject) {
  pushPomPropertyChangePR {
    propertyName = 'keycloak-app.version'
    projects = [
            'fabric8-jenkins/fabric8-jenkins-platform',
            'fabric8io/fabric8-platform'
    ]
    version = stagedProject[1]
  }
}
return this;

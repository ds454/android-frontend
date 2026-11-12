pipeline {
  agent {
    kubernetes {
      yaml '''
        apiVersion: v1
        kind: Pod
        spec:
          containers:
          - name: build
            image: ghcr.io/cirruslabs/android-sdk:36
            command: ["sleep"]
            args: ["infinity"]
      '''
    }
  }
  options {
    skipDefaultCheckout(true)
  }
  stages {
    stage('Checkout') {
      steps {
        checkout([
          $class: 'GitSCM',
          branches: scm.branches,
          userRemoteConfigs: scm.userRemoteConfigs,
          extensions: [[
            $class: 'SubmoduleOption',
            disableSubmodules: false,
            recursiveSubmodules: true,
            parentCredentials: true,
            trackingSubmodules: false,
            reference: '',
            timeout: 30
          ]]
        ])
      }
    }
    stage('Build') {
      steps {
        container('build') {
          script {
            sh """
              chmod +x gradlew
              ./gradlew assembleDebug
          """
          }
        }
      }
    }
  }
}

pipeline {
    agent any

    environment {
        IMAGE_NAME = "been980804/wibee-usersvc"
        DEPLOYMENT_REPO = 'https://github.com/Mi-Ss-A/wibeechat-argocd-config'
        GIT_CREDENTIALS = credentials('git-token')
        TAG = "test-${BUILD_NUMBER}"
        DOCKER_IMAGE = "${IMAGE_NAME}:${TAG}"
    }

    stages {
        stage('Checkout Source') {
            steps {
                checkout scm
            }
        }

        stage('Build Docker image & Push') {
            steps {
                script {
                    docker.withRegistry("https://registry.hub.docker.com", 'docker-token') {
                        def app = docker.build("${DOCKER_IMAGE}", "-f Dockerfile .")
                        app.push()
                    }
                }
            }
        }

        stage('Delete Older Images') {
            steps {
                script {
                    sh '''
                    # 현재 이미지와 직전 버전 제외하고 삭제
                    IMAGE_NAME="${IMAGE_NAME}"
                    KEEP_TAGS=("${TAG}" "test-$(( ${BUILD_NUMBER} - 1 ))")
                    
                    echo "현재 유지할 이미지 태그: ${KEEP_TAGS[@]}"

                    # 이미지 목록 가져오기
                    ALL_IMAGES=$(docker images --format '{{.Repository}}:{{.Tag}}' | grep "$IMAGE_NAME")

                    for IMAGE in $ALL_IMAGES; do
                        DELETE=true
                        for KEEP_TAG in "${KEEP_TAGS[@]}"; do
                            if [[ $IMAGE == *:$KEEP_TAG ]]; then
                                DELETE=false
                                break
                            fi
                        done
                        
                        if $DELETE; then
                            echo "삭제할 이미지: $IMAGE"
                            docker rmi -f $IMAGE || echo "이미지 삭제 실패: $IMAGE"
                        fi
                    done
                    '''
                }
            }
        }

        stage('Update K8S Manifest') {
            steps {
                dir('k8s-manifest') {
                    git url: DEPLOYMENT_REPO, branch: 'test', credentialsId: 'git-token'
                    sh '''
                    sed -i "s|image: .*$|image: ${DOCKER_IMAGE}|" usersvc/deployment.yaml
                    git config user.name "been980804"
                    git config user.email "dlgusqls980804@naver.com"
                    git commit -am "Update usersvc image to ${DOCKER_IMAGE}"
                    git push https://${GIT_CREDENTIALS_USR}:${GIT_CREDENTIALS_PSW}@github.com/Mi-Ss-A/wibeechat-argocd-config test
                    '''
                }
            }
        }
    }
}

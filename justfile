#!/usr/bin/env just --justfile
## Licensed under the terms of http://www.apache.org/licenses/LICENSE-2.0

# NOTE: The just recipes defined below assume sdkman is installed and used for java and maven selection.
#       Recipes that utilize docker containers assume the existence of the specific docker image existing locally

export JAVA_VER_DISTRO_11 := "11.0.18-zulu"
export JAVA_VER_DISTRO_17 := "17.0.6-zulu"
export JAVA_VER_DISTRO_19 := "19.0.2-zulu"
export DOCKER_CMD := "docker container run --rm -it"
export VOL_NAME := "protegeproject-protege"
export M2_REPO := "/root/.m2/repository"
export BLD_DIR := "/usr/src/build"
export IMG := "llsem-ubuntu-maven"

default:
  @echo "Invoke just --list to see a list of possible recipes to run"

clean: clean-11

clean-11:
  #!/usr/bin/env bash -l
  sdk use java ${JAVA_VER_DISTRO_11}
  mvn clean

clean-install: clean-install-11

clean-install-11: clean-11
  #!/usr/bin/env bash -l
  sdk use java ${JAVA_VER_DISTRO_11}
  mvn install

verify: verify-11

verify-11:
  #!/usr/bin/env bash -l
  sdk use java ${JAVA_VER_DISTRO_11}
  mvn verify

dependencies:
  #!/usr/bin/env bash -l
  sdk use java ${JAVA_VER_DISTRO_11}
  mvn dependency:tree -Dscope=compile | tee dependencies.txt

updates:
  #!/usr/bin/env bash -l
  sdk use java ${JAVA_VER_DISTRO_11}
  mvn versions:display-dependency-updates | tee updates.txt

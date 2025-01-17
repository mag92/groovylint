/*
 * Copyright (c) 2019 Ableton AG, Berlin. All rights reserved.
 *
 * Use of this source code is governed by a MIT-style
 * license that can be found in the LICENSE file.
 */

/**
 * Check a set of files with the {@code groovylint} Docker image.
 * @param includesPattern A comma-separated list of Ant-style file patterns to check.
 * @param groovylintImage If specified, use this Docker image handle to run
 *        {@code groovylint}. If {@code null}, then this function will try to fetch
 *        {@code groovylint} from Docker hub using the same version number corresponding
 *        to this library. (default: {@code null})
 */
void check(String includesPattern, Object groovylintImage = null) {
  Object image = groovylintImage
  if (!image) {
    String version = env['library.groovylint.version']
    if (!version) {
      error 'Could not find groovylint version in environment'
    }
    image = docker.image("abletonag/groovylint:${version}")
    image.pull()
  }
  echo "Using groovylint Docker image: ${image.id}"

  image.inside("-v ${env.WORKSPACE}:/ws") {
    sh "python3 /opt/run_codenarc.py -- -includes=${includesPattern}"
  }
}

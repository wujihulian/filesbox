FROM harbor.svnlan.com/library/office6_jdk8_ffmpeg6_libraw_skagent_centos7
COPY target/disk-0.0.1-SNAPSHOT.jar /app.jar
COPY src/main/resources/itest/agent.config /agent/config/agent.config
RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

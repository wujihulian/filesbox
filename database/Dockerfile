FROM harbor.svnlan.com/library/spring_cloud_jdk8_perf_skagent_centos7
COPY target/diskdata-0.0.1-SNAPSHOT.jar /app.jar
COPY src/main/resources/itest/agent.config /agent/config/agent.config
RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

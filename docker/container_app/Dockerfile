############################################################
# Wildfly
############################################################

FROM jboss/wildfly:11.0.0.Final

#RUN cd /opt/jboss/wildfly/standalone/configuration/ && \
#    sed -i 's/enable-welcome-root="true"/enable-welcome-root="false"/g' standalone.xml && \
#    sed -i 's/127.0.0.1/0.0.0.0/g' standalone.xml && \
#    sed -i 's/<deployment-scanner/<deployment-scanner auto-deploy-exploded="true"/g' standalone.xml

# AJP Connector
EXPOSE 8009
# HTTP Connector (for experiment with ALB bypassing Apache)
EXPOSE 8080
# HTTP Management Interface
EXPOSE 9990

############################################################
# App
############################################################

# Add phantomJs

USER root

RUN yum update -y && yum install -y fontconfig && yum install -y freetype; yum clean all
RUN mkdir -p /home/user/apps

# Allow top etc. to run 

RUN export TERM=vt100

# The Wget is commented as the link expires, instead, the phantomjs is added as a binary. 
# Should you wish to download it manually, you can iterate through links from: http://arunoda.me/blog/phantomjs-webfonts-build.html

# -- RUN wget "https://doc-0g-bc-docs.googleusercontent.com/docs/securesc/ha0ro937gcuc7l7deffksulhg5h7mbp1/96a5k4rvi03dfiaq1lur42doqq58pq88/1424599200000/14354319536040687910/*/0B4Wl57IYdOIPLWU0X1l6UzJfeWs?e=download" -O /home/user/apps/phantomjs-1-9-webfonts-linux.zip
# -- RUN unzip /home/user/apps/phantomjs-1-9-webfonts-linux.zip -d /home/user/apps
##COPY phantomjs-1.9.2-linux-x86_64/bin/phantomjs /home/user/apps/
##RUN chmod ugo+x /home/user/apps/phantomjs
##COPY rasterize_with_sessionid.js /home/user/apps/

# Setup Locale

RUN localedef -c -i en_US -f UTF-8 en_US.UTF-8
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8

USER wildfly

# Security
 
# RUN from mgmt-users.properties in /Users/jmcrae/work/bak/goxpro_uat_140626_Thu_161905/home/admin/goxpro/jboss-as-7.1.1.Final/standalone/configuration

# Log Files:

#docker run -it -e JBOSS_LOG_DIR=/opt/wildfly/logs

# Management users (*requires a running jboss)
 
# CMD ./add-user.sh justin superPrivatePwd
# CMD ./add-user.sh geoff reallySuperPrivatePwd 

# Add MySql Driver 
# Note that all additions dump the docker cache
#  Updated 15/02/2015 - Apparently this isn't true, and it's done via Checksum, which is great, but hard to be sure.

##COPY mysql-connector-java-5.1.24.jar /opt/jboss/wildfly/modules/system/layers/base/com/mysql/main/
##COPY mysql-module.xml /opt/jboss/wildfly/modules/system/layers/base/com/mysql/main/module.xml

# Add MariaDB Driver (for use with Aurora) 
# Note that all additions dump the docker cache
#  Updated 15/02/2015 - Apparently this isn't true, and it's done via Checksum, which is great, but hard to be sure.

##COPY mariadb-java-client-2.7.3.jar /opt/jboss/wildfly/modules/system/layers/base/org/mariadb/main/
##COPY mariadb-module.xml /opt/jboss/wildfly/modules/system/layers/base/org/mariadb/main/module.xml

# Is this VOLUME even necessary? Or should we add all of our external volumes here? Does it help document it? 
VOLUME /opt/jboss/wildfly/standalone/configuration

# Not needed in Wildfly 9:
# Make sure signals are passed correctly from standalone.sh to JVM
# This ensures orderly shutdown when docker stop is performed
##ENV LAUNCH_JBOSS_IN_BACKGROUND true

WORKDIR /opt/jboss/wildfly/bin/
# ENV /opt/jdk1.7.0_55/bin/java

# We're exposing the management facilities (management console and JMX) to all addresses, not just 127.0.0.1. 
# The management console and JMX are both protected by username and password (of a management user).
# You should further protect them in production by restricting the incoming IP addresses (we use AWS Security Group).
# To keep the data hidden from man-in-the-middle attacks, connect to ports 9990 and 9999 via an ssh tunnel.

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]

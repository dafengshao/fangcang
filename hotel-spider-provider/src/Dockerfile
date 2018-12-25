FROM 192.168.88.30/startdt/tomcat:base_v1

MAINTAINER YEFEI

RUN mkdir /home/admin/member-provider/{logs,webapps} -p

COPY ./server.xml /usr/local/tomcat/conf/

COPY ./catalina.sh /usr/local/tomcat/bin/catalina.sh

RUN chmod 0750 /usr/local/tomcat/bin/catalina.sh

COPY ./member-provider/target/member-provider.war /home/admin/member-provider/webapps/

WORKDIR /home/admin/member-provider/webapps/

RUN unzip member-provider.war && rm -rf member-provider.war

EXPOSE 8081

CMD /usr/local/tomcat/bin/catalina.sh start && tail -f /usr/local/tomcat/logs/catalina.out

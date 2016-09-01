define nginx::register($content) {
  $ipset = $content
  
  file{"/etc/nginx/sites-available/default":
    owner   => root,
    group   => root,
    ensure  => file,
    mode    => 644,
    content => template('/etc/puppet/templates/etc/nginx/nginx.conf.erb')
  }
}

class gmond {
  file{"/etc/ganglia/gmond.conf":
    owner   => root,
    group   => root,
    ensure  => file,
    mode    => 644,
    content => template('/etc/puppet/templates/etc/ganglia/gmond.conf.erb')
  } 
}

define gmetad::register($ganglia_port, $lb_ipset, $web_ipset, $db_ipset, $deploy_ipset, $monitor_ipset) {
  
  file{"/etc/ganglia/gmetad.conf":
    owner  => root,
    group  => root,
    ensure => file,
    mode   => 644,
    content => template('/etc/puppet/templates/etc/ganglia/gmetad.conf.erb')
  }
}

class mcollective_server {
  file{"/etc/mcollective/facts.yaml":
     owner    => root,
     group    => root,
     mode     => 400,
     loglevel => debug,  # this is needed to avoid it being logged and reported on every run
     # avoid including highly-dynamic facts as they will cause unnecessary template writes
     content  => inline_template("<%= scope.to_hash.reject { |k,v| k.to_s =~ /(uptime_seconds|timestamp|free)/ }.to_yaml %>")
  }
}

class war_file {
  exec{"scp -r -oStrictHostKeyChecking=no deploy.nii.localdomain:/var/tmp/keijiban.war /var/lib/tomcat6/webapps/keijiban.war":
    cwd => "/var/lib/tomcat6/webapps/",
    path => ["/usr/bin", "/usr/sbin/"]
  }
}

class nrpe_server {
  file{"/etc/nagios/nrpe.cfg":
    owner  => root,
    group  => root,
    ensure => file,
    mode   => 644,
    content => template('/etc/puppet/templates/etc/nagios/nrpe.cfg')
  }
}

define nagios::register($lb_ipset, $web_ipset, $db_ipset, $deploy_ipset, $monitor_ipset) {

  file{"/etc/nagios3/nagios.cfg":
    owner  => root,
    group  => root,
    ensure => file,
    mode   => 644,
    content => template('/etc/puppet/templates/etc/nagios/nagios.cfg')
  }
  
  file{"/etc/nagios3/servers/":
    ensure => directory
  }
  
  file{"/etc/nagios3/servers/db.cfg":
    owner  => root,
    group  => root,
    ensure => file,
    mode   => 644,
    content => template('/etc/puppet/templates/etc/nagios/servers/db.cfg.erb')
  }

  file{"/etc/nagios3/servers/lb.cfg":
    owner  => root,
    group  => root,
    ensure => file,
    mode   => 644,
    content => template('/etc/puppet/templates/etc/nagios/servers/lb.cfg.erb')
  }

  file{"/etc/nagios3/servers/web.cfg":
    owner  => root,
    group  => root,
    ensure => file,
    mode   => 644,
    content => template('/etc/puppet/templates/etc/nagios/servers/web.cfg.erb')
  }

  file{"/etc/nagios3/servers/monitor.cfg":
    owner  => root,
    group  => root,
    ensure => file,
    mode   => 644,
    content => template('/etc/puppet/templates/etc/nagios/servers/monitor.cfg.erb')
  }

  file{"/etc/nagios3/servers/deploy.cfg":
    owner  => root,
    group  => root,
    ensure => file,
    mode   => 644,
    content => template('/etc/puppet/templates/etc/nagios/servers/deploy.cfg.erb')
  }

  file{"/etc/nagios3/conf.d/generic-host_nagios2.cfg":
    owner  => root,
    group  => root,
    ensure => file,
    mode   => 644,
    content => template('/etc/puppet/templates/etc/nagios/conf.d/generic-host_nagios2.cfg')
  }

  file{"/etc/nagios3/conf.d/services_nagios2.cfg":
    owner  => root,
    group  => root,
    ensure => file,
    mode   => 644,
    content => template('/etc/puppet/templates/etc/nagios/conf.d/services_nagios2.cfg')
  }

  file{"/etc/nagios3/resource.cfg":
    owner  => root,
    group  => root,
    ensure => file,
    mode   => 644,
    content => template('/etc/puppet/templates/etc/nagios/resource.cfg')
  }

  file{"/etc/nagios3/commands.cfg":
    owner  => root,
    group  => root,
    ensure => file,
    mode   => 644,
    content => template('/etc/puppet/templates/etc/nagios/commands.cfg')
  }

}

$ganglia_port = {
	lb      => 10001,
	web     => 10002,
	db      => 10003,
	mail    => 10004,
	deploy  => 10005,
	monitor => 10006
}

case $hostname {
  'lb': {
     $cluster_name = "LB"
     $cluster_port = $ganglia_port[lb] 
     
     $nginx_ipset = template('/var/tmp/nginx/nginx.ipset')
     nginx::register{"Nginx": content => $nginx_ipset }
     include mcollective_server
     include gmond
  }

  'web': {
    $cluster_name = "Web"
    $cluster_port = $ganglia_port[web]
    include war_file
    include mcollective_server
    include gmond
  } 
  
  'db': {
    $cluster_name = "DB"
    $cluster_port = $ganglia_port[db]

    include mcollective_server
    include gmond
  }

  'mail': {
    $cluster_name = "Mail"
    $cluster_port = $ganglia_port[mail]

    include mcollective_server
  }

  'deploy': {
    $cluster_name = "Deploy"
    $cluster_port = $ganglia_port[deploy]

    include mcollective_server
    include gmond
    #include nrpe_server
  }

  'monitor': {
    $cluster_name = "Monitor"
    $cluster_port = $ganglia_port[monitor]
    $lb_ipset = template('/var/tmp/monitor/lb.ipset')
    $web_ipset = template('/var/tmp/monitor/web.ipset')
    $db_ipset = template('/var/tmp/monitor/db.ipset')
    $deploy_ipset = template('/var/tmp/monitor/deploy.ipset')
    $monitor_ipset = template('/var/tmp/monitor/monitor.ipset')

    include mcollective_server
    include gmond
    gmetad::register{"Ganglia": ganglia_port => $ganglia_port, lb_ipset => $lb_ipset, web_ipset => $web_ipset, db_ipset => $db_ipset, deploy_ipset => $deploy_ipset, monitor_ipset => $monitor_ipset}
    nagios::register{"Nagios": lb_ipset => $lb_ipset, web_ipset => $web_ipset, db_ipset => $db_ipset, deploy_ipset => $deploy_ipset, monitor_ipset => $monitor_ipset}
  }
}

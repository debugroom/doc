#!/bin/bash
# -*- coding: utf-8 -*-

abs_path=$(cd $(dirname $0) && pwd)
prefix_path=$(cd ${abs_path}/../ && pwd)

count=$1

if [ ! "${count}" ]; then
    echo "usage: ./scaleout.sh num :num is number"
    exit 1
fi

# get running web servers information
instance_ids=(`mco facts fqdn -F fqdn=/^web/ -j | ${prefix_path}/bin/retrieve instance_id mco`)
echo ${instance_ids[@]}

# count up running web servers
countup=`expr ${count} - ${#instance_ids[*]}`
if [ ${countup} -gt 0 ]; then
    echo "add webnode ${countup}"
    ${prefix_path}/task/add_webnode_count.sh ${countup}
else
    for ((i=0; $i < ${count}; ++i)); do
	unset instance_ids[$i]
    done
    echo "delete webnode ${instance_ids[@]}"
    ${prefix_path}/task/delete_webnode.sh ${instance_ids[@]}
fi

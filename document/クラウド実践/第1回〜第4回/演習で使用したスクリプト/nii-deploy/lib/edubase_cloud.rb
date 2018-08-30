require 'right_aws'
class EdubaseCloud
  def initialize(access_key, secret_key, endpoint_url)
    @ec2 = RightAws::Ec2.new(access_key, secret_key, {:endpoint_url => endpoint_url, :signature_version => 1})
  end

  def run_instances(image_id, count, group_id, key_name)
    group_ids = [group_id]
    @ec2.run_instances(image_id, count, count, group_ids, key_name)
  end

  def terminate_instances(list=[])
    @ec2.terminate_instances(list)
  end

  def describe_instances(list=[])
    @ec2.describe_instances(list)
  end

  def wait_state_running(list=[])
    state = false
    begin
      sleep 10
      describe_instances(list).each do |instance|
        if 'running' == instance[:aws_state]
          puts "instance state running #{instance[:aws_instance_id]}"
          state = true
        elsif 'pending' == instance[:aws_state]
          state = false
        else
          state = false
          break
        end
      end
    end until state
  end

end

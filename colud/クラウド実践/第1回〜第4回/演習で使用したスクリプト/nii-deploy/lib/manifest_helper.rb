class ManifestHelper

  def template(file)
    templates_dir = File.expand_path('../../templates', __FILE__)
    template_file = "#{templates_dir}/#{file}"
    unless File.exists?(template_file)
      puts "File not found: #{template_file}"
      exit 1
    end
    template_file 
  end

  def output(file) 
    output_dir = "/etc/puppet/templates"
    output_file = "#{output_dir}/#{file}"
  end

  def cmd(command)
     result = `#{command}`
     exit 2 if $? != 0

     begin
       data = JSON.parse(result)
     rescue JSON::ParserError => e
       puts 'JSON Parse error.'
       exit 2
     end
     data
  end

  def bind(template_file, output_file, &block)
    erb = ERB.new(File.new(template_file).read, nil, '-')
    o = File.new(output_file, 'w+')
    o.puts(erb.result(block.call))
    o.close
    puts "output: #{output_file}"
  end

  def parse_facts_ipaddress(res)
    result = []
    h = res.split("\n")
    h.shift
    h.shift
    h.each do |d|
      d.strip!
      result << d.match(/^([0-9.]+).*/)[1]
    end
    result
  end

end


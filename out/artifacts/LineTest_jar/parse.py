def a():
	l = ['paper.txt', 'author.txt', 'term.txt', 'conf.txt']


	for fi in l:
	    s_id = set()
	    print(fi)
	    s_name = set()
	    with open(fi, 'r') as f:
	        for line in f.readlines():
	            id, name = line.split('\t')
	            if name in s_name:
	                print name, 'repetiiiiiit!'

	            s_name.add(name)
	            id = int(id)
	            if id in s_id:
	                print 'id:{} repetidaaaaaaa'.format(id)
	            s_id.add(id)


def b():
	d = {}
	with open('paper.txt', 'r') as f:
		for line in f.readlines():
			id, name = line.split('\t')
			name = name.strip()
			if name in d:
				print 'ja existeix {}, amb id: {}, nova id:{}'.format(name, d[name], id)
			d[name] = id

b()
TARGETS = xml.pdf

all: $(TARGETS)

%.pdf: %.eps
	epstopdf --outfile=$@ --nocompress --res=600 $<

%.eps: %.dia
	dia -n -e $@ -t eps-builtin $<

%.dia: %.dot
	dot -Tdia -o $@ $<

clean:
	rm -f $(TARGETS)

TARGETS = Diagram1.pdf

all: $(TARGETS)

%.pdf: %.eps
	epstopdf --outfile=$@ --nocompress --res=600 $<

%.eps: %.dia
	dia -n -e $@ -t eps-builtin $<

clean:
	rm -f $(TARGETS)

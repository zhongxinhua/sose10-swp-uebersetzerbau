TARGETS = xml.pdf

all: $(TARGETS)

%.pdf: %.eps
	epstopdf --outfile=$@ --nocompress --res=600 $<

%.eps: %.dot
	dot -Teps -o $@ $<

clean:
	rm -f $(TARGETS)

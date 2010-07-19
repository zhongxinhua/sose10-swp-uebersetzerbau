TARGETS = xml.pdf Ausgaben.pdf flowchart.pdf

all: $(TARGETS)

%.pdf: %.eps
	epstopdf --outfile=$@ --nocompress --res=600 $<

%.eps-dia: %.dia
	dia -e $@ $< -t eps-builtin -n

%.eps-dot: %.dot
	dot -o $@ $< -Teps

clean:
	rm -f ./*.pdf
	rm -f ./*.eps
	rm -f ./*.eps-dia
	rm -f ./*.eps-dot

xml.eps: xml.eps-dot
	cp $< $@
Ausgaben.eps: Ausgaben.eps-dot
	cp $< $@
flowchart.eps: flowchart.eps-dia
	cp $< $@

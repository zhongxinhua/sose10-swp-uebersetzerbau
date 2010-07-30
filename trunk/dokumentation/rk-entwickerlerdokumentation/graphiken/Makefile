DIAS = flowchart.pdf Symboltable-inheritance.pdf
DOTS = xml.pdf Ausgaben.pdf
SVGS = Fub-logo.pdf
TARGETS = $(DIAS) $(DOTS) $(SVGS)

all: $(TARGETS)
dots: $(DOTS)
dias: $(DIAS)
svgs: $(SVGS)

%.pdf: %.eps
	epstopdf --outfile=$@ --nocompress --res=600 $<

$(DIAS:pdf=eps): %.eps: %.dia
	dia -n -t eps-builtin -e $@ $<

$(DOTS:pdf=eps): %.eps: %.dot
	dot -Teps -o $@ $<

$(SVGS:pdf=eps): %.eps: %.svg
	inkscape -z -d 600 -E $@ $<

clean:
	$(RM) $(TARGETS)
	$(RM) $(TARGETS:pdf=eps)

clean-dias:
	$(RM) $(DIAS)
	$(RM) $(DIAS:pdf=eps)

clean-dots:
	$(RM) $(DOTS)
	$(RM) $(DOTS:pdf=eps)

clean-svgs:
	$(RM) $(DOTS)
	$(RM) $(DOTS:pdf=eps)

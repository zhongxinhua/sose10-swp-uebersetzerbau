DIAS = flowchart.pdf
DOTS = xml.pdf Ausgaben.pdf
TARGETS = $(DIAS) $(DOTS)

all: $(TARGETS)
dots: $(DOTS)
dias: $(DIAS)

%.pdf: %.eps
	epstopdf --outfile=$@ --nocompress --res=600 $<

$(DIAS:pdf=eps): %.eps: %.dia
	dia -t eps-builtin -n -e $@ $<

$(DOTS:pdf=eps): %.eps: %.dot
	dot -Teps -o $@ $<

clean:
	$(RM) $(TARGETS)
	$(RM) $(TARGETS:pdf=eps)

clean-dias:
	$(RM) $(DIAS)
	$(RM) $(DIAS:pdf=eps)

clean-dots:
	$(RM) $(DOTS)
	$(RM) $(DOTS:pdf=eps)

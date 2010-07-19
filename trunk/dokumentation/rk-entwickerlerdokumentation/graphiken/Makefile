DIAS = flowchart.pdf
DOTS = xml.pdf Ausgaben.pdf
TARGETS = $(DIAS) $(DOTS)

all: dots dias
dots: $(DOTS) .PHONY
dias: $(DIAS) .PHONY

%.pdf: %.eps
	epstopdf --outfile=$@ --nocompress --res=600 $<

$(DIAS:pdf=eps): %.eps: %.dia
	dia -e $@ $< -t eps-builtin -n

$(DOTS:pdf=eps): %.eps: %.dot
	dot -o $@ $< -Teps

clean:
	rm -f $(TARGETS)
	rm -f $(TARGETS:pdf=eps)

.PHONY:

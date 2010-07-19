TARGETS = report.pdf
SUBS = flowchart.MAKE-SUB xml.MAKE-SUB

all: $(TARGETS) $(SUBS)

report.pdf: report.tex jhla4.sty flowchart.MAKE-SUB xml.MAKE-SUB

%.pdf: %.tex
	rm -f $(patsubst %.pdf,%.lol,$@)
	rm -f $(patsubst %.pdf,%.lof,$@)
	rm -f $(patsubst %.pdf,%.toc,$@)
	pdflatex $<
	pdflatex $<

clean: clean-temps clean-subs
	rm -f texput.log

clean-temps: $(patsubst %.pdf,%.CLEAN-TMP,$(TARGETS))

clean-subs: $(patsubst %.MAKE-SUB,%.CLEAN-DIR,$(SUBS))

# Pseudo-Targets:

%.MAKE-SUB :
	cd $(patsubst %.MAKE-SUB,%,$@) ; $(MAKE) all

%.CLEAN-TMP:
	rm -f $(patsubst %.CLEAN-TMP,%.aux,$@)
	rm -f $(patsubst %.CLEAN-TMP,%.log,$@)
	rm -f $(patsubst %.CLEAN-TMP,%.lof,$@)
	rm -f $(patsubst %.CLEAN-TMP,%.lol,$@)
	rm -f $(patsubst %.CLEAN-TMP,%.out,$@)
	rm -f $(patsubst %.CLEAN-TMP,%.pdf,$@)
	rm -f $(patsubst %.CLEAN-TMP,%.toc,$@)

%.CLEAN-DIR:
	cd $(patsubst %.CLEAN-DIR,%,$@) ; $(MAKE) clean

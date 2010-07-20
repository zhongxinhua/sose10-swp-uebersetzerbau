TARGETS = report.pdf
SUBS = graphiken.MAKE-SUB

all: $(TARGETS) $(SUBS)

report.pdf: report.tex jhla4.sty graphiken.MAKE-SUB

%.pdf: %.tex
	$(RM) $(patsubst %.pdf,%.aux,$@)
	$(RM) $(patsubst %.pdf,%.lol,$@)
	$(RM) $(patsubst %.pdf,%.lof,$@)
	$(RM) $(patsubst %.pdf,%.out,$@)
	$(RM) $(patsubst %.pdf,%.toc,$@)
	pdflatex -halt-on-error -interaction=errorstopmode $<
	pdflatex -halt-on-error -interaction=errorstopmode $<

clean: clean-temps clean-subs
	$(RM) texput.log

clean-temps: $(TARGETS:pdf=CLEAN-TMP)

clean-subs: $(SUBS:MAKE-SUB=CLEAN-DIR)

# Pseudo-Targets:

%.MAKE-SUB:
	cd $(patsubst %.MAKE-SUB,%,$@) ; $(MAKE) all

%.CLEAN-TMP:
	$(RM) $(patsubst %.CLEAN-TMP,%.aux,$@)
	$(RM) $(patsubst %.CLEAN-TMP,%.log,$@)
	$(RM) $(patsubst %.CLEAN-TMP,%.lof,$@)
	$(RM) $(patsubst %.CLEAN-TMP,%.lol,$@)
	$(RM) $(patsubst %.CLEAN-TMP,%.out,$@)
	$(RM) $(patsubst %.CLEAN-TMP,%.pdf,$@)
	$(RM) $(patsubst %.CLEAN-TMP,%.toc,$@)

%.CLEAN-DIR:
	cd $(patsubst %.CLEAN-DIR,%,$@) ; $(MAKE) clean

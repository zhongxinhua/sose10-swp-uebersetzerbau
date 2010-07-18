report.pdf: report.tex jhla4.sty
	rm -f report.toc
	pdflatex report.tex
	pdflatex report.tex

clean: .PHONY
	rm -rf report.aux report.log report.out report.pdf report.toc texput.log

.PHONY:

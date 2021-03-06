//******************************************************************************
//
// Filename :       adc.c
// Dependencies:    none
// Processor:       PIC18
// Complier:        MCC18 v3.40 or higher
// Company:         HVA.
// Auther:	    Ferry
// Date:            2012-02-09
// Version:         1.0
//
//
//******************************************************************************

#include <p18f4550.h>
#include "myadc.h"

void ADC_init();
int getAD(char adcselector);



//******************************************************************************
//  adc_init
//
//  Inits analog ports this is really important because the analog ports
//  are multiplexed with the digitalports failing to initialize will
//  result in erratic behavior.
//******************************************************************************

void ADC_init() {
    //Configure analog pins, voltage reference and digital IO, ADCON1
    TRISA = 0xDF; // All input port A5 as output
    ADCON1 = 0x0B; //Use AN0 to AN3, 4 channel

    //Select AD acquisition time and AD conversion clock, ADCON2
    ADCON2 = 0xA6;

    //Turn on AD module, ADCON0 bit 0;
    ADCON0 |= 0x1;
}

int getAD(char adcselector) {
    // set channel to ad0, start conversion
    ADCON0 = adcselector;
    //wait for adconversion to be done
    while (ADCON0 & 2) {
        ;
    } //nop

    // 10 bit A/D last 2 bit are higest bits
    return ((unsigned int) ADRESH << 8) | (ADRESL);
}
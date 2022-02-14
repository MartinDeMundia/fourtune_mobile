/*
  Copyright 2008 Google Inc.
  
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  
       http://www.apache.org/licenses/LICENSE-2.0
  
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.

  Modified by Curtis Gedak 2015, 2016
*/
package com.coreictconsultancy.solitaire;

import android.os.Bundle;

import java.util.Stack;


public abstract class Rules {

  public static final int FORTYTHIEVES = 1;
  public static final int FREECELL     = 2;
  public static final int GOLF         = 3;
  public static final int KLONDIKE     = 4;
  public static final int SPIDER       = 5;
  public static final int TRIPEAKS     = 6;

  public static final int EVENT_INVALID = -1;
  public static final int EVENT_DEAL = 1;
  public static final int EVENT_STACK_ADD = 2;
  public static final int EVENT_FLING = 3;
  public static final int EVENT_SMART_MOVE = 4;
  public static final int EVENT_DEAL_NEXT = 5;
  public static final int EVENT_STACK_TAP = 6;

  public static final int AUTO_MOVE_ALWAYS = 2;
  public static final int AUTO_MOVE_FLING_ONLY = 1;
  public static final int AUTO_MOVE_NEVER = 0;

  private int mType;
  protected SolitaireView mView;
  protected Stack<com.coreictconsultancy.solitaire.Move> mMoveHistory;
  protected com.coreictconsultancy.solitaire.AnimateCard mAnimateCard;
  protected boolean mIgnoreEvents;
  protected com.coreictconsultancy.solitaire.EventPoster mEventPoster;
  protected boolean mWon;


  // Anchors
  protected com.coreictconsultancy.solitaire.CardAnchor[] mCardAnchor;
  protected int mCardAnchorCount;

  protected com.coreictconsultancy.solitaire.Deck mDeck;
  protected int mCardCount;

  // Automove
  protected int mAutoMoveLevel;
  protected boolean mWasFling;

  public boolean HasWon() { return mWon; }
  public int GetType() { return mType; }
  public int GetCardCount() { return mCardCount; }
  public com.coreictconsultancy.solitaire.CardAnchor[] GetAnchorArray() { return mCardAnchor; }
  public void SetType(int type) { mType = type; }
  public void SetView(SolitaireView view) { mView = view; }
  public void SetMoveHistory(Stack<com.coreictconsultancy.solitaire.Move> moveHistory) { mMoveHistory = moveHistory; }
  public void SetAnimateCard(com.coreictconsultancy.solitaire.AnimateCard animateCard) { mAnimateCard = animateCard; }
  public void SetIgnoreEvents(boolean ignore) { mIgnoreEvents = ignore; }
  public void SetEventPoster(com.coreictconsultancy.solitaire.EventPoster ep) { mEventPoster = ep; }
  public boolean GetIgnoreEvents() { return mIgnoreEvents; }
  public int GetRulesExtra() { return 0; }
  public String GetGameTypeString() { return ""; }
  public String GetPrettyGameTypeString() { return ""; }
  public boolean HasScore() { return false; }
  public boolean HasString() { return false; }
  public String GetString() { return ""; }
  public void SetCarryOverScore(int score) {}
  public int GetScore() { return 0; }
  public void AddDealCount() {}
  public void MarkBlockedCards() {}

  // Free Spaces are used in games like Forty Thieves and Freecell
  public int CountFreeSpaces() { return 0; }
  public int CountFreeSpacesMin() { return CountFreeSpaces(); }

  protected void SignalWin() { mWon = true; mView.MarkWin(); mView.DisplayWin(); }

  abstract public void Init(Bundle map);
  public void EventAlert(int event) { if (!mIgnoreEvents) { mEventPoster.PostEvent(event); mView.Refresh(); } }
  public void EventAlert(int event, com.coreictconsultancy.solitaire.CardAnchor anchor) { if (!mIgnoreEvents) { mEventPoster.PostEvent(event, anchor);  mView.Refresh();} }
  public void EventAlert(int event, com.coreictconsultancy.solitaire.CardAnchor anchor, com.coreictconsultancy.solitaire.Card card) { if (!mIgnoreEvents) { mEventPoster.PostEvent(event, anchor, card);  mView.Refresh();} }
  public void ClearEvent() { mEventPoster.ClearEvent(); }
  abstract public void EventProcess(int event, com.coreictconsultancy.solitaire.CardAnchor anchor);
  abstract public void EventProcess(int event, com.coreictconsultancy.solitaire.CardAnchor anchor, com.coreictconsultancy.solitaire.Card card);
  abstract public void EventProcess(int event);
  abstract public void Resize(int width, int height);
  public boolean Fling(com.coreictconsultancy.solitaire.MoveCard moveCard) { moveCard.Release(); return false; }
  public void HandleEvents() { 
    while (!mIgnoreEvents && mEventPoster.HasEvent()) {
      mEventPoster.HandleEvent();
    }
  }

  public void RefreshOptions() {
    mAutoMoveLevel = Integer.parseInt(mView.GetSettings().getString("AutoMoveLevel", "1" /* default - fling only */));
    mWasFling = false;
  }

  public static com.coreictconsultancy.solitaire.Rules CreateRules(int type, Bundle map, SolitaireView view,
                                                               Stack<com.coreictconsultancy.solitaire.Move> moveHistory, com.coreictconsultancy.solitaire.AnimateCard animate) {
    com.coreictconsultancy.solitaire.Rules ret = null;
    switch (type) {
      case FORTYTHIEVES:
        ret = new com.coreictconsultancy.solitaire.RulesFortyThieves();
        break;
      case FREECELL:
        ret = new com.coreictconsultancy.solitaire.RulesFreecell();
        break;
      case GOLF:
        ret = new com.coreictconsultancy.solitaire.RulesGolf();
        break;
      case KLONDIKE:
        ret = new RulesKlondike();
        break;
      case SPIDER:
        ret = new com.coreictconsultancy.solitaire.RulesSpider();
        break;
      case TRIPEAKS:
        ret = new com.coreictconsultancy.solitaire.RulesTriPeaks();
        break;
    }

    if (ret != null) {
      ret.mWon = false;
      ret.SetType(type);
      ret.SetView(view);
      ret.SetMoveHistory(moveHistory);
      ret.SetAnimateCard(animate);
      ret.SetEventPoster(new com.coreictconsultancy.solitaire.EventPoster(ret));
      ret.RefreshOptions();
      ret.Init(map);
    }
    return ret;
  }

  public void FinishDeal() {};  // Used when multi-stack deal interrupted
}


class EventPoster {
  private int mEvent;
  private com.coreictconsultancy.solitaire.CardAnchor mCardAnchor;
  private com.coreictconsultancy.solitaire.Card mCard;
  private com.coreictconsultancy.solitaire.Rules mRules;

  public EventPoster(com.coreictconsultancy.solitaire.Rules rules) {
    mRules = rules;
    mEvent = -1;
    mCardAnchor = null;
    mCard = null;
  }

  public void PostEvent(int event) {
    PostEvent(event, null, null);
  }

  public void PostEvent(int event, com.coreictconsultancy.solitaire.CardAnchor anchor) {
    PostEvent(event, anchor, null);
  }

  public void PostEvent(int event, com.coreictconsultancy.solitaire.CardAnchor anchor, com.coreictconsultancy.solitaire.Card card) {
    mEvent = event;
    mCardAnchor = anchor;
    mCard = card;
  }


  public void ClearEvent() {
    mEvent = com.coreictconsultancy.solitaire.Rules.EVENT_INVALID;
    mCardAnchor = null;
    mCard = null;
  }

  public boolean HasEvent() {
    return mEvent != com.coreictconsultancy.solitaire.Rules.EVENT_INVALID;
  }

  public void HandleEvent() {
    if (HasEvent()) {
      int event = mEvent;
      com.coreictconsultancy.solitaire.CardAnchor cardAnchor = mCardAnchor;
      com.coreictconsultancy.solitaire.Card card = mCard;
      ClearEvent();
      if (cardAnchor != null && card != null) {
        mRules.EventProcess(event, cardAnchor, card);
      } else if (cardAnchor != null) {
        mRules.EventProcess(event, cardAnchor);
      } else {
        mRules.EventProcess(event);
      }
    }
  }
}



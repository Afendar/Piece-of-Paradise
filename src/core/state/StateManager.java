package core.state;

import core.Context;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import states.GameState;
import states.LoseState;
import states.ReportState;
import states.StartState;
import states.WinState;

public class StateManager
{
    private final Map<StateType, BaseState> m_statesFactory;
    private final Context m_context;
    private final Map<StateType, BaseState> m_states;
    
    public StateManager(Context context)
    {
        m_statesFactory = new HashMap<>();
        m_states = new LinkedHashMap<>();
        m_context = context;
        
        registerState(StateType.GAME);
        registerState(StateType.REPORT);
        registerState(StateType.LOSE);
        registerState(StateType.START);
        registerState(StateType.WIN);
    }
    
    public void update(double dt)
    {
        if(m_states.isEmpty())
        {
            return;
        }
        
        BaseState[] states = m_states.values().toArray(new BaseState[0]);
        
        if(states[states.length - 1].isTranscendent() && states.length > 1)
        {
            int i;
            for(i = states.length - 1 ; i >= 0 ; --i)
            {
                if(!states[i].isTranscendent())
                {
                    break;
                }
            }
            for( ; i < states.length ; i++)
            {
                states[i].update(dt);
            }
        }
        else
        {
            states[m_states.size() - 1].update(dt);
        }
    }
    
    public void render(Graphics2D g)
    {
        if(m_states.isEmpty())
        {
            return;
        }
        
        BaseState[] states = m_states.values().toArray(new BaseState[0]);
        
        if(states[states.length - 1].isTransparent() && states.length > 1)
        {
            int i;
            for(i = states.length - 1 ; i >= 0 ; --i)
            {
                if(!states[i].isTransparent())
                {
                    break;
                }
            }
            for( ; i < states.length ; i++)
            {
                states[i].render(g);
            }
        }
        else
        {
            ((BaseState)m_states.values().toArray()[m_states.size() - 1]).render(g);
        }
    }
    
    public void switchTo(StateType type)
    {
        if(!m_states.isEmpty())
        {
            ((BaseState)m_states.values().toArray()[m_states.size() - 1]).desactivate();
        }
        
        if(m_states.containsKey(type))
        {
            BaseState states = (BaseState)m_states.values().toArray()[m_states.size() - 1];
            states.desactivate();
            
            BaseState tmpState = m_states.get(type);
            m_states.remove(type);
            m_states.put(type, tmpState);
            tmpState.activate();
            return;
        }
        
        createState(type);
        ((BaseState)m_states.values().toArray()[m_states.size() - 1]).activate();
    }
    
    public BaseState getState(StateType type)
    {
        if(m_states.isEmpty())
        {
           return null; 
        }
        
        BaseState search = m_statesFactory.get(type);
        
        if(m_states.containsKey(type))
        {
            return m_states.get(type);
        }
        return null;
    }
    
    public void remove(StateType type)
    {
        removeState(type);
    }
    
    private void createState(StateType type)
    {       
        BaseState s = m_statesFactory.get(type);
        if(s == null)
        {
            return;
        }
        
        m_states.putIfAbsent(type, s);
        s.onCreate();
    }
    
    private void removeState(StateType type)
    {
        m_states.get(type).onDestroy();
        m_states.remove(type);
    }
    
    private void registerState(StateType type)
    {
        switch(type)
        {
            case GAME:
                m_statesFactory.put(StateType.GAME, new GameState(this));
                break;
            case REPORT:
                m_statesFactory.put(StateType.REPORT, new ReportState(this));
                break;
            case LOSE:
                m_statesFactory.put(StateType.LOSE, new LoseState(this));
                break;
            case START:
                m_statesFactory.put(StateType.START, new StartState(this));
                break;
            case WIN:
                m_statesFactory.put(StateType.WIN, new WinState(this));
                break;
        }
    }
    
    public Context getContext()
    {
        return m_context;
    }
}
